FROM python:3.12-slim
WORKDIR /app

# System deps for psycopg2/building wheels and playwright
RUN apt-get update && \
    apt-get install -y --no-install-recommends build-essential libpq-dev curl && \
    rm -rf /var/lib/apt/lists/*

# Install python deps
# When docker build context is ./scrapy/scrapy_cotizador, copy requirements.txt from the context root
COPY requirements.txt /app/requirements.txt
RUN pip install --upgrade pip && pip install -r /app/requirements.txt

# Install playwright browsers
RUN python -m playwright install chromium

# Copy project (copy everything from build context into /app)
COPY . /app

CMD ["python", "run_quick_test.py"]
