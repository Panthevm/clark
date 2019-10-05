FROM nginx
EXPOSE 80
COPY build/ /usr/share/nginx/html

COPY entrypoint.sh /
RUN chmod +x /entrypoint.sh
ENTRYPOINT ["/entrypoint.sh"]