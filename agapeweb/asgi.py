"""
ASGI config for agapeweb project.

It exposes the ASGI callable as a module-level variable named ``application``.

For more information on this file, see
https://docs.djangoproject.com/en/4.0/howto/deployment/asgi/
"""

import os

from django.core.asgi import get_asgi_application
from channels.routing import ProtocolTypeRouter, URLRouter
from channels.http import AsgiHandler
from channels.auth import AuthMiddlewareStack

import agape_sockets.routers

os.environ.setdefault('DJANGO_SETTINGS_MODULE', 'agapeweb.settings')

application = ProtocolTypeRouter({
    # 'http': AsgiHandler()
    # We will add WebSocket protocol later, but for now it's just HTTP.
    'http':get_asgi_application(),
    'websocket':AuthMiddlewareStack(
        URLRouter(
            agape_sockets.routers.websocket_urlpatterns
        )
    )
})

# application = get_asgi_application()
