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
from channels.security.websocket import AllowedHostsOriginValidator

os.environ.setdefault('DJANGO_SETTINGS_MODULE', 'agapeweb.settings')
django_asgi_app = get_asgi_application()

import agape_sockets.routing

application = ProtocolTypeRouter({
    "http":django_asgi_app,
    "websocket":AuthMiddlewareStack(
            URLRouter(
                agape_sockets.routing.websocket_urlpatterns
            )
        )  
    
})

