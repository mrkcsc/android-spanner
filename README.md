### Android Spanner

A powerful spannable string builder that supports arbitrary substitutions.

### Installation

[![Download](https://api.bintray.com/packages/mrkcsc/maven/com.miguelgaeta.spanner/images/download.svg)](https://bintray.com/mrkcsc/maven/com.miguelgaeta.spanner/_latestVersion)

```groovy

compile 'com.miguelgaeta.android-spanner:spanner:1.0.1'

```

```groovy

compile 'com.miguelgaeta.android-spanner:spanner:1.0.1'

```

### Usage

TODO

```java

    final SpannableString spannableString =
        new Spanner("Hey this is totally @{1121212} cool *miguel*!")
            .addReplacementStrategy(new Spanner.OnMatchListener() {
                @Override
                public Spanner.Replacement call(String match) {
                    return new Spanner.Replacement(match, SpanHelpers.createBoldSpan());
                }
            }, "*", "*")
            .addReplacementStrategy(new Spanner.OnMatchListener() {
                @Override
                public Spanner.Replacement call(String match) {
                    return new Spanner.Replacement("Sam",
                        SpanHelpers.createForegroundColorSpan(MainActivity.this, android.R.color.holo_red_dark),
                        SpanHelpers.createBoldItalicSpan());
                }
            }, "@{", "}")
            .toSpannableString();

```

### License

*Copyright 2016 Miguel Gaeta*

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
