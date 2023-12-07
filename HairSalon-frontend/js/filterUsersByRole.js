function filterUsersByRole(data, role) {
  if (role === "ALL") {
    return data;
  } else {
    return data.filter((User) => User.role === role);
  }
}
