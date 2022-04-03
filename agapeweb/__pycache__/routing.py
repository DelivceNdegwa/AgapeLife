from channels.routing import route
from agape_sockets.consumers import ws_connect, ws_disconnect

channel_routing = [
    route('websockets.connect', ws_connect),
    route('websockets.disconnect', ws_disconnect),
]