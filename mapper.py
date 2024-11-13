# mapper.py
import sys

# 输入来自标准输入（stdin）
for line in sys.stdin:
    line = line.strip()
    words = line.split()
    for word in words:
        print(f"{word}\t1")
