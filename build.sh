javac -d bin $(find src -name '*.java')
java -DdeveloperMode=true -cp bin Main
