# Generated by Django 4.0.3 on 2022-04-06 23:51

from django.db import migrations


class Migration(migrations.Migration):

    dependencies = [
        ('staff', '0003_doctor_loggedindoctor_appointment_doctor_and_more'),
    ]

    operations = [
        migrations.RenameModel(
            old_name='DoctorPrescriptions',
            new_name='DoctorPrescription',
        ),
    ]
