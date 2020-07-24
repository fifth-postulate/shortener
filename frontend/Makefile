.PHONY: all

SOURCE_DIR=src
BUILD_DIR=web/js

all: ${BUILD_DIR}/Main.min.js ${BUILD_DIR}/Main.dev.js image
	@echo "finished building"

image: Dockerfile
	@docker build -t dvberkel/shortener_frontend .

${BUILD_DIR}/%.min.js: ${BUILD_DIR}/%.js
	uglifyjs $< --compress 'pure_funcs="F2,F3,F4,F5,F6,F7,F8,F9,A2,A3,A4,A5,A6,A7,A8,A9",pure_getters,keep_fargs=false,unsafe_comps,unsafe' | uglifyjs --mangle --output=$@

${BUILD_DIR}/%.js: ${SOURCE_DIR}/%.elm
	elm make $< --optimize --output=$@

${BUILD_DIR}/%.dev.js: ${SOURCE_DIR}/%.elm
	elm make $< --debug --output=$@