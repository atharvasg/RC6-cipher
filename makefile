all:
	javac *.java 
	java Driver Input.txt output.txt
clean:
	rm -f Driver.class Writer.class RCImplementor.class
