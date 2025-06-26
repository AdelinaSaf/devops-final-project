FROM python:3.12-slim
RUN apt-get update && apt-get install -y \
    libjpeg-dev \
    zlib1g-dev
WORKDIR /app/
COPY requirements.txt .
RUN pip install --no-cache-dir -r requirements.txt
COPY . .
EXPOSE 5050
CMD ["python3", "app.py"]