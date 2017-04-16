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
package rx.receiver.local.android;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Scheduler;
import io.reactivex.functions.Cancellable;

import static io.reactivex.android.schedulers.AndroidSchedulers.mainThread;

public class RxReceiverLocal {
    /**
     *
     * @param context
     * @param intentFilter
     * @return
     */
    @NonNull
    public static Observable<Intent> receives(@NonNull final Context context, @NonNull final IntentFilter intentFilter) {
        return Observable.create(new ObservableOnSubscribe<Intent>() {
            @Override public void subscribe(final ObservableEmitter<Intent> emitter) {
                final BroadcastReceiver receiver = new BroadcastReceiver() {
                    @Override public void onReceive(Context context, Intent intent) {
                        emitter.onNext(intent);
                    }
                };

                LocalBroadcastManager.getInstance(context).registerReceiver(receiver, intentFilter);

                emitter.setCancellable(new Cancellable() {
                    @Override
                    public void cancel() throws Exception {
                        if (Looper.getMainLooper() == Looper.myLooper()) {
                            LocalBroadcastManager.getInstance(context).unregisterReceiver(receiver);
                        } else {
                            final Scheduler.Worker inner = mainThread().createWorker();
                            inner.schedule(new Runnable() {
                                @Override public void run() {
                                    LocalBroadcastManager.getInstance(context).unregisterReceiver(receiver);
                                    inner.dispose();
                                }
                            });
                        }
                    }
                });
            }
        });
    }
}
