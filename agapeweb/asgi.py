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

import agape_sockets.routing

os.environ.setdefault('DJANGO_SETTINGS_MODULE', 'agapeweb.settings')

application = ProtocolTypeRouter({
    "http":get_asgi_application(),
    "websocket":AllowedHostsOriginValidator(
        AuthMiddlewareStack(
            URLRouter(
                agape_sockets.routing.websocket_urlpatterns
            )
        )
    )
})

