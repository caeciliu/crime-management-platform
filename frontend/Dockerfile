FROM nginx:alpine

# 删除默认的nginx配置
RUN rm -rf /usr/share/nginx/html/*

# 复制本地构建的前端文件
COPY dist/ /usr/share/nginx/html/

# 复制nginx配置
COPY nginx.conf /etc/nginx/nginx.conf

EXPOSE 80

CMD ["nginx", "-g", "daemon off;"]