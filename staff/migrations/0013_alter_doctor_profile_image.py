# Generated by Django 4.0.3 on 2022-04-05 00:50

from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('staff', '0012_alter_doctor_profile_image'),
    ]

    operations = [
        migrations.AlterField(
            model_name='doctor',
            name='profile_image',
            field=models.ImageField(blank=True, null=True, upload_to='media/'),
        ),
    ]
