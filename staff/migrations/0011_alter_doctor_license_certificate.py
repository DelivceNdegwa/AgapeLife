# Generated by Django 4.0.3 on 2022-04-05 00:46

from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('staff', '0010_alter_doctor_profile_image'),
    ]

    operations = [
        migrations.AlterField(
            model_name='doctor',
            name='license_certificate',
            field=models.FileField(blank=True, null=True, upload_to='license/'),
        ),
    ]
