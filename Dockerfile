FROM ubuntu:latest
LABEL authors="fernando"

ENTRYPOINT ["top", "-b"]