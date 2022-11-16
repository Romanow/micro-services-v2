output "vm_ip" {
  value       = digitalocean_droplet.main.ipv4_address
  description = "Droplet ip address"
}