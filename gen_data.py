#!/usr/bin/python
from PyProbs import Probability as pr

def zero_or_one(p):
    if (pr.Prob(f'{p}%')):
        return 0
    else:
        return 1

democrat = [53,7,58,85,63,83,75,89,84,52,70,61,68,29,77,94,12,68,82,13,39,65,19,59,82]
republican = [5,86,8,7,7,32,13,23,23,10,12,97,7,80,31,32,77,23,21,67,5,18,74,84,33]
libertarian = [16,37,32,15,10,27,20,32,20,25,27,79,28,49,71,32,63,24,22,57,10,37,25,50,35]
socialist = [54,7,54,75,68,87,89,89,79,55,79,65,79,16,74,92,11,74,83,14,61,75,10,27,83]

p = socialist

row = [0] * len(p)

for x in range(0, 1000):
    for i in range(0, len(p)):
        row[i] = zero_or_one(p[i])
    print(','.join(map(str, row)) )
