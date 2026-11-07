package model.stock;

public class Proveedor {
    private String cuit;
    private String razonSocial;
    private String direccion;
    private String email;
    private String telefono;

    public Proveedor(String cuit, String razonSocial, String direccion,
                     String email, String telefono) {
        if (cuit == null || cuit.isBlank()) {
            throw new IllegalArgumentException("El CUIT es obligatorio");
        }
        this.cuit = cuit;
        this.razonSocial = razonSocial;
        this.direccion = direccion;
        this.email = email;
        this.telefono = telefono;
    }

    public String getCuit() { return cuit; }
    public String getRazonSocial() { return razonSocial; }
    public String getDireccion() { return direccion; }
    public String getEmail() { return email; }
    public String getTelefono() { return telefono; }

    public void setDireccion(String direccion) { this.direccion = direccion; }
    public void setEmail(String email) { this.email = email; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    // Formato: cuit;razonSocial;direccion;email;telefono
    public String toLinea() {
        return cuit + ";" + razonSocial + ";" + direccion + ";" + email + ";" + telefono;
    }

    public static Proveedor desdeLinea(String linea) {
        String[] p = linea.split(";", -1);
        return new Proveedor(p[0], p[1], p[2], p[3], p[4]);
    }
}