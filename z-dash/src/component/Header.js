import React from "react";
import {AppBar, Toolbar, Typography} from "@material-ui/core";
import Grid from "@material-ui/core/Grid";

export default props => (
    <Grid item xs={12}>
      <AppBar position="static">
          <Toolbar>
              <Typography variant="subtitle1" color="inherit">Dash</Typography>
          </Toolbar>
      </AppBar>
    </Grid>
);