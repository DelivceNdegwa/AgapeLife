"""
ASGI config for agapeweb project.

It exposes the ASGI callable as a module-level variable named ``application``.

For more information on this file, see
https://docs.djangoproject.com/en/4.0/howto/deployment/asgi/
"""

import os

from django.core.asgi import get_asgi_application
from channels.routing import ProtocolTypeRouter
from channels.http import AsgiHandler

os.environ.setdefault('DJANGO_SETTINGS_MODULE', 'agapeweb.settings')

application = ProtocolTypeRouter({
    'http': AsgiHandler()
    # We will add WebSocket protocol later, but for now it's just HTTP.
})

# application = get_asgi_application()
