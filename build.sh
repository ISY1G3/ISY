#!/bin/sh
set -eu

exec ./mvnw javafx:run -DdeveloperMode=true
