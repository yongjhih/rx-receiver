# RxReceiver

[![CircleCI](https://circleci.com/gh/yongjhih/rx-receiver.svg?style=shield)](https://circleci.com/gh/yongjhih/rx-receiver)
[![codecov](https://codecov.io/gh/yongjhih/rx-receiver/branch/master/graph/badge.svg)](https://codecov.io/gh/yongjhih/rx-receiver)

RxJava1/2 for Android BroadcastReceiver. It's easier to extend for that based on BroadcastReceiver.

Who extend:

* [RxNet](https://github.com/yongjhih/rx-net) (including RxConnectivity/RxWifi)
* RxTelephony
* RxBattery

## Usage

rx2-receiver:

```java
RxReceiver.receives(context, intentFilter).subscribe();
```

rx2-receiver-kotlin:

```kt
intentFilter.receives(context).subscribe()
```

rx2-receiver-local (v4):

```java
RxReceiverLocal.receives(context, intentFilter).subscribe();
```

rx2-receiver-local-kotlin (v4):

```kt
intentFilter.receivesLocal(context).subscribe()
```

## Installation

RxJava2

```gradle
repositories {
    jcenter()
    maven { url "https://jitpack.io" }
}

dependencies {
    compile 'com.github.yongjhih.rx-receiver:rx2-receiver:-SNAPSHOT'
    compile 'com.github.yongjhih.rx-receiver:rx2-receiver-local:-SNAPSHOT'
    // compile 'com.github.yongjhih.rx-receiver:rx2-receiver-kotlin:-SNAPSHOT' // optional
    // compile 'com.github.yongjhih.rx-receiver:rx2-receiver-local-kotlin:-SNAPSHOT' // optional
}
```

RxJava1

```gradle
repositories {
    jcenter()
    maven { url "https://jitpack.io" }
}

dependencies {
    compile 'com.github.yongjhih.rx-receiver:rx-receiver:-SNAPSHOT'
    compile 'com.github.yongjhih.rx-receiver:rx-receiver-local:-SNAPSHOT'
    // compile 'com.github.yongjhih.rx-receiver:rx-receiver-kotlin:-SNAPSHOT' // optional
    // compile 'com.github.yongjhih.rx-receiver:rx-receiver-local-kotlin:-SNAPSHOT' // optional
}
```

## LICENSE

Apache 2.0
