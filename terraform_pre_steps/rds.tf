
resource "aws_db_instance" "rds" {
  instance_class = "db.t2.micro"
  engine = "mysql"
  engine_version = "5.7"
  storage_type = "gp2"
  allocated_storage = 20
  publicly_accessible = "true"
  name = "terrasitemap"
  username = "admin"
  password = "anadoraletca"
  apply_immediately = "true"
  identifier = "terrasitemap"
  skip_final_snapshot  = true
}
