from rest_framework import serializers
from .models import TestSocketModel


class TestSocketModelSerializer(serializers.ModelSerializer):
    
    class Meta:
        model = TestSocketModel
        fields = ['name']