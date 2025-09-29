FROM node:22-alpine3.22 AS build
WORKDIR /app
COPY package*.json ./
RUN npm install
COPY . .
RUN npm run build --configurations=production

FROM nginx:stable-alpine
#COPY ./nginx.conf /etc/nginx/conf.d/default.conf
COPY --from=build /app/dist/high-roller-angular-app/browser /usr/share/nginx/html

EXPOSE 80
