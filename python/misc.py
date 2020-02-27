ts('obama', 20, False, True)

p = (170, 0.1, 0.6)
if p[1] >= 0.5:
    print('Whew, that is bright!')
if p[2] >= 0.5:
    print('Wow, that is light')

def get_quotes(*args):
    'Return a dictionary of real-time stock quotes'
    return {symbol: get_quote(symbol) for symbol in args}

for interface, status in interfaces:
    if status == 'up':
        print(interface)