data "digitalocean_ssh_key" "default" {
  name = "[work-pc] Romanow"
}

data "digitalocean_image" "ubuntu" {
  name = var.vm_image
}

resource "digitalocean_droplet" "main" {
  image    = data.digitalocean_image.ubuntu.id
  name     = var.hostname
  region   = var.vm_region
  size     = var.vm_size
  ssh_keys = [data.digitalocean_ssh_key.default.fingerprint]
}

resource "digitalocean_record" "public" {
  domain = var.domain
  name   = var.hostname
  type   = "A"
  ttl    = 300
  value  = digitalocean_droplet.main.ipv4_address
}