twitter_search('obama', numtweets=20, retweets=False, unicode=True)

from collections import namedtuple

Color = namedtuple('Color', ['hue', 'saturation', 'luminosity'])

p = Color(170, 0.1, 0.6)
if p.saturation >= 0.5:
    print('Whew, that is bright!')
if p.luminosity >= 0.5:
    print('Wow, that is light')

def get_quotes(*symbols):
    'Return a dictionary of real-time stock quotes'
    return {symbol: get_quote(symbol) for symbol in symbols}

for interface, status in interfaces:
    if status == 'up':
        print(interface)