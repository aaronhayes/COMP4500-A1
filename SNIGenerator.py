def generate_SNI(num):
    student = "98" + num + "52"
    d = []
    for digit in student:
        d.append(int(digit))
    print d

    for i in range(2, 12):
        if d[i] == d[i - 1]:
            d[i] = (d[i] + 3) % 10
    print d


generate_SNI('42338468')