pushd fieldhealer/src/org/indiahackathon/healingfields/server/diagnoser/
javac WebMdAdapter.java -classpath ../../../../../../war/WEB-INF/lib/json-simple-1.1.1.jar
java -classpath .:../../../../../../war/WEB-INF/lib/json-simple-1.1.1.jar WebMdAdapter 
popd
