<!-- the "home-page" element is passed as a parameter to VueComponent in the JavalinConfig file -->
<template id="home-page">
  <app-layout>
    <div class="row">
      <div class="col">
        <div class="card">
          <h5 class="card-header">Registered Users</h5>
          <div class="card-body">
            <h5 class="card-title">{{users.length}} users</h5>
            <a href="/users" class="btn btn-primary">More Details...</a>
          </div>
        </div>
      </div>
      <div class="col">
        <div class="card">
          <h5 class="card-header">Total Activities</h5>
          <div class="card-body">
            <h5 class="card-title">{{activities.length}} activities</h5>
            <a href="/activities" class="btn btn-primary">More Details...</a>
          </div>
        </div>
      </div>
    </div>
    <div class="row">
      <div class="col">
        <div class="card">
          <h5 class="card-header">Health Parameters</h5>
          <div class="card-body">
            <h5 class="card-title">{{healthparameters.length}} entries</h5>
            <a href="/healthparameters" class="btn btn-primary">More Details...</a>
          </div>
        </div>
      </div>
      <div class="col">
        <div class="card">
          <h5 class="card-header">Sleep Monitor</h5>
          <div class="card-body">
            <h5 class="card-title">{{sleepmonitor.length}} entries</h5>
            <a href="/sleepmonitor" class="btn btn-primary">More Details...</a>
          </div>
        </div>
      </div>
    </div>
    <div class="row">
      <div class="col">
        <div class="card">
          <h5 class="card-header">Measurements</h5>
          <div class="card-body">
            <h5 class="card-title">{{measurements.length}} entries</h5>
            <a href="/measurements" class="btn btn-primary">More Details...</a>
          </div>
        </div>
      </div>
      <div class="col">
        <div class="card">
          <h5 class="card-header">Exercise Tracker (Gym)</h5>
          <div class="card-body">
            <h5 class="card-title">{{exercise.length}} entries</h5>
            <a href="/exercise" class="btn btn-primary">More Details...</a>
          </div>
        </div>
      </div>
    </div>
  </app-layout>
</template>

<script>
Vue.component('home-page',
    {
      template: "#home-page",
      data: () => ({
        users: [],
        activities: [],
        healthparameters:[],
        sleepmonitor:[],
        measurements:[],
        exercise:[]
      }),
      created() {
        axios.get("/api/users")
            .then(res => this.users = res.data)
            .catch(() => alert("Error while fetching users"));
        axios.get("/api/activities")
            .then(res => this.activities = res.data)
            .catch(() => alert("Error while fetching activities"));
        axios.get("/api/healthparameters")
            .then(res => this.healthparameters = res.data)
            .catch(() => alert("Error while fetching healthparameters"));
        axios.get("/api/sleepmonitoring")
            .then(res => this.sleepmonitor = res.data)
            .catch(() => alert("Error while fetching sleepmonitor information"));
        axios.get("/api/measurements")
            .then(res => this.measurements = res.data)
            .catch(() => alert("Error while fetching measurements information"));
        axios.get("/api/exercisetracker")
            .then(res => this.exercise = res.data)
            .catch(() => alert("Error while fetching exercise information"));
      }
    });
</script>