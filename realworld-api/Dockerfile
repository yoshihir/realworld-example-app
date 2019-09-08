FROM amazoncorretto:latest

WORKDIR /tmp

COPY docker-entrypoint.sh /usr/local/bin
RUN chmod +x /usr/local/bin/docker-entrypoint.sh

COPY target/universal/ /tmp
RUN yum install -y unzip \
    && unzip -o 'realworld-api-*.zip' \
    && rm -f realworld-api-*.zip \
    && mv realworld-api-* realworld-api \
    && mkdir -p /var/apps/play \
    && mv realworld-api/* /var/apps/play/ \
    && yum clean all

WORKDIR /var/apps/play

ENTRYPOINT ["docker-entrypoint.sh"]