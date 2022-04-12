# Generated by Django 4.0.3 on 2022-04-06 23:28

from django.db import migrations, models
import django.db.models.deletion


class Migration(migrations.Migration):

    dependencies = [
        ('staff', '0001_initial'),
    ]

    operations = [
        migrations.CreateModel(
            name='Appointment',
            fields=[
                ('id', models.BigAutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('about', models.CharField(max_length=200)),
                ('start_time', models.DateTimeField()),
                ('end_time', models.DateTimeField()),
                ('status', models.CharField(choices=[(4, 'Pending'), (2, 'Ongoing'), (1, 'Complete'), (3, 'Cancelled')], default=4, max_length=30)),
                ('created_at', models.DateTimeField(auto_now=True)),
                ('updated_at', models.DateTimeField(auto_now_add=True)),
            ],
        ),
        migrations.CreateModel(
            name='DoctorPrescriptions',
            fields=[
                ('id', models.BigAutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('medicine', models.TextField(blank=True, null=True)),
                ('recommendation', models.TextField()),
                ('created_at', models.DateTimeField(auto_now=True)),
            ],
        ),
        migrations.CreateModel(
            name='PatientSymptoms',
            fields=[
                ('id', models.BigAutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('symptoms', models.TextField()),
            ],
        ),
        migrations.RemoveField(
            model_name='loggedindoctor',
            name='doctor',
        ),
        migrations.RemoveField(
            model_name='loggedindoctor',
            name='user',
        ),
        migrations.AlterModelOptions(
            name='agapeuser',
            options={'verbose_name': 'Agape User'},
        ),
        migrations.RemoveField(
            model_name='doctorcategorypivot',
            name='doctor',
        ),
        migrations.RemoveField(
            model_name='editordoctorpivot',
            name='doctor',
        ),
        migrations.AddField(
            model_name='medicaltips',
            name='sourced_from',
            field=models.TextField(blank=True, null=True),
        ),
        migrations.AddField(
            model_name='userfeedback',
            name='category',
            field=models.CharField(choices=[(1, 'Doctor Related'), (2, 'App Related')], default=2, max_length=30),
        ),
        migrations.AddField(
            model_name='userfeedback',
            name='status',
            field=models.CharField(choices=[(1, 'Read'), (1, 'Unread')], default=1, max_length=30),
        ),
        migrations.DeleteModel(
            name='Doctor',
        ),
        migrations.DeleteModel(
            name='LoggedInDoctor',
        ),
        migrations.AddField(
            model_name='patientsymptoms',
            name='patient',
            field=models.ForeignKey(null=True, on_delete=django.db.models.deletion.SET_NULL, to='staff.agapeuser'),
        ),
        migrations.AddField(
            model_name='doctorprescriptions',
            name='prescription_to',
            field=models.ForeignKey(on_delete=django.db.models.deletion.CASCADE, to='staff.agapeuser'),
        ),
        migrations.AddField(
            model_name='appointment',
            name='client',
            field=models.ForeignKey(null=True, on_delete=django.db.models.deletion.SET_NULL, to='staff.agapeuser'),
        ),
    ]
