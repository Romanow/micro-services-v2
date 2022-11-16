data "digitalocean_ssh_key" "default" {
  name = "[work-pc] Romanow"
}

resource "digitalocean_droplet" "main" {
  image    = var.vm_image
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