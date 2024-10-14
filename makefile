# Makefile to compile Java source files from the 'src' folder and output to 'classes' folder

SRC_DIR = src
OUT_DIR = classes
PORT_NO = 3000
HOSTNAME = localhost
COOKIE_FILE_PATH=/Users/kennethphang/Projects/SDF-workshop-day06/cookie_file.txt

SOURCES = $(shell find $(SRC_DIR) -name "*.java")

JAVAC = javac
JFLAGS = -d $(OUT_DIR) -cp $(OUT_DIR)
JAVA = java
CLIENT_APP = sg.edu.nus.iss.client.ClientApp
SERVER_APP = sg.edu.nus.iss.server.ServerApp

all: $(OUT_DIR) compile

$(OUT_DIR):
	@mkdir -p $(OUT_DIR)

compile: $(SOURCES)
	$(JAVAC) $(JFLAGS) $(SOURCES)
	cp cookie_file.txt $(OUT_DIR)

run-client: compile
	$(JAVA) -cp $(OUT_DIR) $(CLIENT_APP) ${HOSTNAME}:${PORT_NO}

run-server: compile
	$(JAVA) -cp $(OUT_DIR) $(SERVER_APP) ${PORT_NO} ${COOKIE_FILE_PATH}

clean:
	@rm -rf $(OUT_DIR)

.PHONY: all compile clean run-client run-server