# Generated by Django 4.0.3 on 2022-05-30 09:22

from django.db import migrations


class Migration(migrations.Migration):

    dependencies = [
        ('agape_sockets', '0008_remove_doctor1_category'),
    ]

    operations = [
        migrations.RemoveField(
            model_name='doctor1',
            name='created_at',
        ),
        migrations.RemoveField(
            model_name='doctor1',
            name='updated_at',
        ),
    ]
