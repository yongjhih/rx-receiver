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

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Looper;
import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;

import rx.Emitter;
import rx.Observable;
import rx.Scheduler;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Cancellable;

import static rx.android.schedulers.AndroidSchedulers.mainThread;

public class RxReceiver {
    /**
     *
     * @param context
     * @param intentFilter
     * @return
     */
    @NonNull
    @CheckResult
    public static Observable<Intent> receives(@NonNull final Context context, @NonNull final IntentFilter intentFilter) {
        return Observable.create(new Action1<Emitter<Intent>>() {
            @Override
            public void call(final Emitter<Intent> emitter) {
                final BroadcastReceiver receiver = new BroadcastReceiver() {
                    @Override public void onReceive(Context context, Intent intent) {
                        emitter.onNext(intent);
                    }
                };

                emitter.setCancellation(new Cancellable() {
                    @Override
                    public void cancel() throws Exception {
                        if (Looper.getMainLooper() == Looper.myLooper()) {
                            context.unregisterReceiver(receiver);
                        } else {
                            final Scheduler.Worker inner = mainThread().createWorker();
                            inner.schedule(new Action0() {
                                @Override public void call() {
                                    context.unregisterReceiver(receiver);
                                    inner.unsubscribe();
                                }
                            });
                        }
                    }
                });

                context.registerReceiver(receiver, intentFilter);
            }
        }, Emitter.BackpressureMode.BUFFER);
    }
}
