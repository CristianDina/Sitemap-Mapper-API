resource "aws_instance" "ec2" {
  ami = "ami-0e2ab4ec1609a6006"
  instance_type = "t2.micro"
  iam_instance_profile = "${aws_iam_instance_profile.iam_profile.name}"
  key_name = "jennew"
}

resource "aws_iam_instance_profile" "iam_profile" {
  name = "TerraECSRoleForEC2"
  role = "${aws_iam_role.iam_role.name}"
}