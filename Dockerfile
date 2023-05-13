FROM clojure
RUN mkdir -p /usr/src
WORKDIR /usr/src
COPY project.clj /usr/src
RUN lein deps
COPY . /usr/src
CMD ["lein", "run"]