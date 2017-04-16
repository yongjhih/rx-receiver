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
package rx2.receiver.local.android;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import io.reactivex.observers.TestObserver;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class RxReceiverLocalTest {
    @Test
    public void test() {
        IntentFilter intentFilter = new IntentFilter("test");
        Context context = RuntimeEnvironment.application.getApplicationContext();

        TestObserver<Intent> tester = RxReceiverLocal.receives(context, intentFilter)
                .test();
        Intent foobar = new Intent("test").putExtra("foo", "bar");
        LocalBroadcastManager.getInstance(context).sendBroadcast(foobar);
        tester.assertValues(foobar);

        Intent barbaz = new Intent("test").putExtra("bar", "baz");
        LocalBroadcastManager.getInstance(context).sendBroadcast(barbaz);
        tester.assertValues(foobar, barbaz);

        Intent nullIntent = new Intent("test_null").putExtra("bar", "baz");
        LocalBroadcastManager.getInstance(context).sendBroadcast(nullIntent);
        tester.assertValues(foobar, barbaz);

        LocalBroadcastManager.getInstance(context).sendBroadcast(barbaz);
        tester.assertValues(foobar, barbaz, barbaz);
    }
}
