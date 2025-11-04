# Stage 1: Build the React app
FROM node:18 AS build

# Set working directory
WORKDIR /app

# Copy package.json and install dependencies
COPY package*.json ./
RUN npm install

# Copy the rest of the app source code
COPY . .

# Build the app for production
RUN npm run build

# Stage 2: Serve the app with Nginx
FROM nginx:alpine

# Copy custom nginx.conf from src folder
COPY src/nginx.conf /etc/nginx/conf.d/default.conf

# Copy built static files
COPY --from=build /app/build /usr/share/nginx/html

# Expose port (React default: 3000, Nginx default: 80)
EXPOSE 80

# Start Nginx server
CMD ["nginx", "-g", "daemon off;"]
