# Generated by Django 4.0.3 on 2022-05-27 19:58

from django.conf import settings
import django.contrib.auth.models
from django.db import migrations, models
import django.db.models.deletion


class Migration(migrations.Migration):

    dependencies = [
        ('auth', '0012_alter_user_first_name_max_length'),
        ('staff', '0017_appointmentrequest'),
        ('agape_sockets', '0005_appointmentrequest1'),
    ]

    operations = [
        migrations.CreateModel(
            name='AgapeUser1',
            fields=[
                ('user_ptr', models.OneToOneField(auto_created=True, on_delete=django.db.models.deletion.CASCADE, parent_link=True, primary_key=True, serialize=False, to=settings.AUTH_USER_MODEL)),
                ('phone_number', models.IntegerField()),
                ('id_number', models.IntegerField()),
            ],
            options={
                'verbose_name': 'Agape User',
            },
            bases=('auth.user',),
            managers=[
                ('objects', django.contrib.auth.models.UserManager()),
            ],
        ),
        migrations.CreateModel(
            name='PatientSymptoms1',
            fields=[
                ('id', models.BigAutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('symptoms', models.TextField()),
                ('patient', models.ForeignKey(null=True, on_delete=django.db.models.deletion.SET_NULL, to='staff.agapeuser')),
                ('viewed_by', models.ForeignKey(blank=True, null=True, on_delete=django.db.models.deletion.SET_NULL, to='staff.doctor')),
            ],
            options={
                'verbose_name_plural': 'Patient Symptoms',
            },
        ),
        migrations.CreateModel(
            name='Doctor1',
            fields=[
                ('user_ptr', models.OneToOneField(auto_created=True, on_delete=django.db.models.deletion.CASCADE, parent_link=True, primary_key=True, serialize=False, to=settings.AUTH_USER_MODEL)),
                ('is_verified', models.BooleanField(default=False)),
                ('license_certificate', models.FileField(blank=True, null=True, upload_to='license/')),
                ('profile_image', models.ImageField(blank=True, null=True, upload_to='media/')),
                ('hospital', models.CharField(max_length=200)),
                ('experience_years', models.IntegerField(blank=True, null=True)),
                ('speciality', models.CharField(max_length=100)),
                ('phone_number', models.IntegerField(null=True)),
                ('id_number', models.IntegerField(null=True)),
                ('self_description', models.TextField(blank=True, null=True)),
                ('is_available', models.BooleanField(default=False)),
                ('created_at', models.DateTimeField(auto_now=True)),
                ('updated_at', models.DateTimeField(auto_now_add=True)),
                ('category', models.ForeignKey(blank=True, null=True, on_delete=django.db.models.deletion.SET_NULL, to='staff.medicalcategory')),
            ],
            options={
                'verbose_name': 'Agape Doctor',
            },
            bases=('auth.user',),
            managers=[
                ('objects', django.contrib.auth.models.UserManager()),
            ],
        ),
        migrations.AlterField(
            model_name='appointmentrequest1',
            name='client',
            field=models.ForeignKey(on_delete=django.db.models.deletion.CASCADE, to='agape_sockets.agapeuser1'),
        ),
        migrations.AlterField(
            model_name='appointmentrequest1',
            name='doctor',
            field=models.ForeignKey(on_delete=django.db.models.deletion.CASCADE, to='agape_sockets.doctor1'),
        ),
        migrations.AlterField(
            model_name='appointmentrequest1',
            name='symptoms',
            field=models.ForeignKey(blank=True, null=True, on_delete=django.db.models.deletion.PROTECT, to='agape_sockets.patientsymptoms1'),
        ),
    ]
