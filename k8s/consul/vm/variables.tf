variable "do_token" {
  type        = string
  description = "DigitalOcean access token"
}

variable "domain" {
  type        = string
  default     = "romanow-alex.ru"
  description = "Base Domain name"
}

variable "vm_image" {
  type        = string
  default     = "ubuntu-22-04-x64"
  description = "VM image name"
}

variable "vm_region" {
  type        = string
  default     = "ams3"
  description = "VM region"
}

variable "vm_size" {
  type        = string
  default     = "s-1vcpu-1gb"
  description = "Node size"
}

variable "hostname" {
  type        = string
  default     = "vm"
  description = "VM hostname"
}