# Generated by Django 4.0.3 on 2022-05-06 14:59

from django.db import migrations


class Migration(migrations.Migration):

    dependencies = [
        ('staff', '0009_alter_userfeedback_category_and_more'),
    ]

    operations = [
        migrations.DeleteModel(
            name='AppointmentToken',
        ),
    ]
