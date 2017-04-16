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

import android.app.Application;
import android.content.Intent;
import android.content.IntentFilter;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import rx.Subscription;
import rx.observers.TestSubscriber;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class RxReceiverTest {
    @Test public void subscribe() {
        IntentFilter intentFilter = new IntentFilter("test_action");
        Application application = RuntimeEnvironment.application;

        TestSubscriber<Intent> o = new TestSubscriber<>();
        Subscription subscription = RxReceiver.receives(application, intentFilter).subscribe(o);
        o.assertValues();

        Intent intent1 = new Intent("test_action").putExtra("foo", "bar");
        application.sendBroadcast(intent1);
        o.assertValues(intent1);

        Intent intent2 = new Intent("test_action").putExtra("bar", "baz");
        application.sendBroadcast(intent2);
        o.assertValues(intent1, intent2);

        Intent intent3 = new Intent("test_action_ignored");
        application.sendBroadcast(intent3);
        o.assertValues(intent1, intent2);

        Intent intent4 = new Intent("test_action").putExtra("bar", "baz");
        subscription.unsubscribe();
        application.sendBroadcast(intent4);
        o.assertValues(intent1, intent2);
    }
}