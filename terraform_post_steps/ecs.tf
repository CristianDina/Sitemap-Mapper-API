//
//data "aws_ecs_task_definition" "ecs" {
//  task_definition = aws_ecs_task_definition.ecs.family
//}

resource "aws_ecs_task_definition" "ecs" {
  family = "TerraECS"
  container_definitions = <<DEFINITION
  [
    {
      "name": "TerraECRContainer",
      "image": "360679213856.dkr.ecr.eu-west-2.amazonaws.com/terraecr:latest",
      "memory": 500,
      "essential": true,
      "portMappings": [
        {
          "containerPort": 8080,
          "hostPort": 8080
        }
      ]
    }
  ]
DEFINITION
}