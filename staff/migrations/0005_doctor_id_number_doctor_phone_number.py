# Generated by Django 4.0.3 on 2022-04-06 23:58

from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('staff', '0004_rename_doctorprescriptions_doctorprescription'),
    ]

    operations = [
        migrations.AddField(
            model_name='doctor',
            name='id_number',
            field=models.IntegerField(null=True),
        ),
        migrations.AddField(
            model_name='doctor',
            name='phone_number',
            field=models.IntegerField(null=True),
        ),
    ]
