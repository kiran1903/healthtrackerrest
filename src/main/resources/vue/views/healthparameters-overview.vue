<template id="healthparameters-overview">
  <div>
    <h3>HealthParameters Information </h3>
    <ul>
      <li v-for="healthparameter in healthparameters">
        {{healthparameter.id}}: {{healthparameter.bloodPressure}}, {{healthparameter.pulse}}, {{healthparameter.glucose}} measured on {{healthparameter.measuredOn}}
      </li>
    </ul>
  </div>
</template>

<script>
Vue.component("healthparameters-overview",{
  template: "#healthparameters-overview",
  data: () => ({
    healthparameterrs: [],
  }),
  created() {
    axios.get(`/api/healthparameters`)
        .then(res => this.healthparameterrs = res.data)
        .catch(() => alert("Error while fetching healthparameters"));
  }
});
</script>