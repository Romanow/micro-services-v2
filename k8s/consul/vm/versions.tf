terraform {
  required_providers {
    digitalocean = {
      source  = "digitalocean/digitalocean"
      version = ">= 2.23"
    }
  }
  required_version = ">= 1.3"
}

terraform {
  backend "local" {}
}

provider "digitalocean" {
  token = var.do_token
}