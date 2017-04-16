/*
 * Copyright (C) 2017, Andrew Chen
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package rx.receiver.android;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import org.assertj.core.api.ThrowableAssert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import io.reactivex.observers.TestObserver;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.assertj.core.api.Java6Assertions.assertThatThrownBy;
import static org.assertj.core.api.Java6Assertions.fail;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class RxReceiverTest {
    @Test
    public void testReceivesNull() {
        //assertThatThrownBy(new ThrowableAssert.ThrowingCallable() {
        //    @Override
        //    public void call() throws Throwable {
        //        RxReceiver.receives(null, null);
        //    }
        //}).isInstanceOf(NullPointerException.class);

        //try {
        //    RxReceiver.receives(null, null);
        //    fail("No NPE thrown");
        //} catch (Throwable e) {
        //    assertThat(e).isInstanceOf(NullPointerException.class);
        //}

        RxReceiver.receives(null, null).test().assertError(NullPointerException.class);
    }

    @Test
    public void test() {
        IntentFilter intentFilter = new IntentFilter("test");
        Context context = RuntimeEnvironment.application.getApplicationContext();

        TestObserver<Intent> tester = RxReceiver.receives(context, intentFilter)
                .test();
        Intent foobar = new Intent("test").putExtra("foo", "bar");
        context.sendBroadcast(foobar);
        tester.assertValues(foobar);

        Intent barbaz = new Intent("test").putExtra("bar", "baz");
        context.sendBroadcast(barbaz);
        tester.assertValues(foobar, barbaz);

        Intent nullIntent = new Intent("test_null").putExtra("bar", "baz");
        context.sendBroadcast(nullIntent);
        tester.assertValues(foobar, barbaz);

        context.sendBroadcast(barbaz);
        tester.assertValues(foobar, barbaz, barbaz);
    }
}