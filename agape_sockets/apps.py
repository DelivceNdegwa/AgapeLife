from django.apps import AppConfig


class AgapeSocketsConfig(AppConfig):
    default_auto_field = 'django.db.models.BigAutoField'
    name = 'agape_sockets'
    
    def ready(self):
        import agape_sockets.signals
        
