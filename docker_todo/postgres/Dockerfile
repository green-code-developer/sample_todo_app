FROM postgres:11.5-alpine
ENV LANG ja_JP.utf8
ENV TZ Asia/Tokyo
ADD ./DDL/ /docker-entrypoint-initdb.d/
