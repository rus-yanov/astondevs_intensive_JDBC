build:
	./gradlew war

clean:
	./gradlew clean

lint:
	./gradlew checkstyleMain checkstyleTest

test:
	./gradlew test

report:
	./gradlew jacocoTestReport

