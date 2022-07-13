from django.test import TestCase


## doctors
    # path("doctors", doctorsList, name="doctors-list"),
    # path("doctors/<id>", doctorDetails, name="doctor-details"),
    # path("verify-doctor/<id>", verifyDoctor, name="verify-doctor"),
    # path("unverify-doctor/<id>", deverifyDoctor, name="unverify-doctor"),


class URLTests(TestCase):
    def test_indexurl(self):
        response = self.client.get('/staff/')
        self.assertEqual(response.status_code, 200)
        
    def test_doctorslisturl(self):
        response = self.client.get('/staff/doctors')
        self.assertEqual(response.status_code, 200)
